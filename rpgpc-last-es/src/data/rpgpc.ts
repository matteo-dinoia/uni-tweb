export interface PartyI {
    name: string;
    shortname: string;
    characters?: number[];
}

export interface CharacterI {
    id: number,
    name: string,
    level: number,
    rpgclass: string,
    description: string[];
}